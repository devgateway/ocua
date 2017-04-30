import {Map} from "immutable";
import {pluck, range} from "../tools";
import Table from "../visualizations/tables/index";
import ReactDOMServer from "react-dom/server";
import CustomPopupChart from "./custom-popup-chart";
import INDICATOR_NAMES from "./indicator-names";

const pluckObj = (field, obj) => Object.keys(obj).map(key => obj[key][field]);

//copypasted from https://www.sitepoint.com/javascript-generate-lighter-darker-color/
function colorLuminance(hex, lum) {

  // validate hex string
  hex = String(hex).replace(/[^0-9a-f]/gi, '');
  if (hex.length < 6) {
    hex = hex[0]+hex[0]+hex[1]+hex[1]+hex[2]+hex[2];
  }
  lum = lum || 0;

  // convert to decimal and change luminosity
  var rgb = "#", c, i;
  for (i = 0; i < 3; i++) {
    c = parseInt(hex.substr(i*2,2), 16);
    c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
    rgb += ("00"+c).substr(c.length);
  }

  return rgb;
}

class CorruptionType extends CustomPopupChart{
  groupData(data){
    let grouped = {
      COLLUSION: {},
      FRAUD: {},
      RIGGING: {}
    };
    const {monthly} = this.props;
    data.forEach(datum => {
      const type = datum.get('type');
      let date;
      if(monthly){
        const month = datum.get('month');
        date = this.t(`general:months:${month}`);
      } else {
        date = datum.get('year');
      }
      grouped[type] = grouped[type] || {};
      grouped[type][date] = datum.toJS();
    });

    return grouped;
  }

  getData(){
    const data = super.getData();
    if(!data) return [];
    const {styling, months, monthly, years} = this.props;
    const grouped = this.groupData(data);
    return Object.keys(grouped).map((type, index) => {
      const dataForType = grouped[type];
      let values = [], dates = [];
      if(monthly){
        dates = range(1, 12)
          .filter(month => months.has(month))
          .map(month => this.t(`general:months:${month}`));

        values = dates.map(month => dataForType[month] ? dataForType[month].flaggedCount : 0);
      } else {
        dates = years.sort().toArray();
        values = dates.map(year => dataForType[year] ? dataForType[year].flaggedCount : 0);
      }

      if(dates.length == 1){
        dates.unshift("");
        dates.push(" ");
        values.unshift(0);
        values.push(0);
      }

      return {
        x: dates,
        y: values,
        type: 'scatter',
        fill: 'tonexty',
        name: type,
        fillcolor: styling.charts.traceColors[index],
        line: {
          color: colorLuminance(styling.charts.traceColors[index], -.3)
        }
      }
    });
  }

  getLayout(){
    return {
      hovermode: 'closest',
      xaxis: {
        type: 'category'
      },
      yaxis: {},
      legend: {
        orientation: 'h',
        xanchor: 'right',
        yanchor: 'top',
        x: 1,
        y: 1.3
      }
    }
  }

  getPopup(){
    const {popup} = this.state;
    const {year, traceName: corruptionType} = popup;
    const {indicatorTypesMapping} = this.props;
    const data = this.groupData(super.getData());
    if(!data[corruptionType]) return null;
    const dataForPoint = data[corruptionType][year];
    if(!dataForPoint) return null;
    const indicatorCount =
      Object.keys(indicatorTypesMapping).filter(indicatorId =>
        indicatorTypesMapping[indicatorId].types.indexOf(dataForPoint.type) > -1
      ).length;

    return (
      <div className="crd-popup" style={{top: popup.top, left: popup.left}}>
        <div className="row">
          <div className="col-sm-12 info text-center">
            {year}
          </div>
          <div className="col-sm-12">
            <hr/>
          </div>
          <div className="col-sm-7 text-right title">Indicators</div>
          <div className="col-sm-5 text-left info">{indicatorCount}</div>
          <div className="col-sm-7 text-right title">Total Flags</div>
          <div className="col-sm-5 text-left info">{dataForPoint.flaggedCount}</div>
          <div className="col-sm-7 text-right title">Total Projects Flagged</div>
          <div className="col-sm-5 text-left info">{dataForPoint.flaggedProjectCount}</div>
          <div className="col-sm-7 text-right title">% Total Projects Flagged</div>
          <div className="col-sm-5 text-left info">{dataForPoint.percent.toFixed(2)}%</div>
        </div>
        <div className="arrow"/>
      </div>
    )
  }
}

CorruptionType.endpoint = 'percentTotalProjectsFlaggedByYear';

class TopFlaggedContracts extends Table{
  row(entry, index){
    const tenderValue = entry.getIn(['tender', 'value']);
    const awardValue = entry.getIn(['awards', 0, 'value']);
    const tenderPeriod = entry.get('tenderPeriod');
    const startDate = new Date(tenderPeriod.get('startDate'));
    const endDate = new Date(tenderPeriod.get('endDate'));
    const flags = entry.get('flags');
    const flaggedStats = flags.get('flaggedStats');
    const type = flaggedStats.get('type');
    const flagIds =
      flags
        .filter(
          flag => flag.has('types') && flag.get('types').includes(type) && flag.get('value')
        )
        .keySeq();

    const procuringEntityName = entry.getIn(['procuringEntity', 'name']);

    return (
      <tr key={index}>
        <td>{entry.get('tag', []).join(', ')}</td>
        <td>{entry.get('ocid')}</td>
        <td>{entry.get('title')}</td>
        <td>
          <div title={procuringEntityName} className="oce-3-line-text">
            {procuringEntityName}
          </div>
        </td>
        <td>{tenderValue && tenderValue.get('amount')} {tenderValue && tenderValue.get('currency')}</td>
        <td>{awardValue.get('amount')} {awardValue.get('currency')}</td>
        <td>{startDate.toLocaleDateString()}&mdash;{endDate.toLocaleDateString()}</td>
        <td>{type}</td>
        <td className="hoverable popup-left">
          {flaggedStats.get('count')}
          <div className="crd-popup text-center">
            <div className="row">
              <div className="col-sm-12 info">
                Associated {type[0] + type.substr(1).toLowerCase()} Flags
              </div>
              <div className="col-sm-12">
                <hr/>
              </div>
              <div className="col-sm-12 info">
                {flagIds.map(flagId => <p key={flagId}>{INDICATOR_NAMES[flagId].name}</p>)}
              </div>
            </div>
            <div className="arrow"/>
          </div>
        </td>
      </tr>
    )
  }

  render(){
    const {data} = this.props;
    return (
      <table className="table table-striped table-hover table-top-flagged-contracts">
        <thead>
          <tr>
            <th>Status</th>
            <th>Contract ID</th>
            <th>Title</th>
            <th>Procuring Entity</th>
            <th>Tender Amount</th>
            <th>Awards Amount</th>
            <th>Tender Date</th>
            <th className="flag-type">Flag Type</th>
            <th>No. of Flags</th>
          </tr>
        </thead>
        <tbody>
          {data && data.map(this.row.bind(this))}
        </tbody>
      </table>
    )
  }
}

TopFlaggedContracts.endpoint = 'corruptionRiskOverviewTable?pageSize=10';

class OverviewPage extends React.Component{
  constructor(...args){
    super(...args);
    this.state = {
      corruptionType: null,
      topFlaggedContracts: null
    }
  }

  render(){
    const {corruptionType, topFlaggedContracts} = this.state;
    const {filters, translations, years, monthly, months, indicatorTypesMapping, styling, width} = this.props;
    return (
      <div className="page-overview">
        <section className="chart-corruption-types">
          <h3 className="page-header">Risk of Fraud, Collusion and Process Rigging Over Time</h3>
          <CorruptionType
              filters={filters}
              requestNewData={(_, corruptionType) => this.setState({corruptionType})}
              translations={translations}
              data={corruptionType}
              years={years}
              monthly={monthly}
              months={months}
              styling={styling}
              indicatorTypesMapping={indicatorTypesMapping}
              width={width}
              margin={{t: 0, r: 0, b: 0, r: 0, pad: 40}}
          />
        </section>
        <section>
          <h3 className="page-header">The Projects with the Most Fraud, Collusion and Process Rigging Flags</h3>
            <TopFlaggedContracts
                filters={filters}
                data={topFlaggedContracts}
                translations={translations}
                years={years}
                monthly={monthly}
                months={months}
                requestNewData={(_, topFlaggedContracts) => this.setState({topFlaggedContracts})}
            />
        </section>
      </div>
    )
  }
}

export default OverviewPage;
