import FrontendYearFilterableChart from "./frontend-filterable";
import {pluckImm} from "../../tools";

class PercentEbid extends FrontendYearFilterableChart{
  getData(){
    let data = super.getData();
    if(!data) return [];
    return [{
      x: data.map(pluckImm('year')).toArray(),
      y: data.map(pluckImm('percentageTendersUsingEbid')).toArray(),
      type: 'scatter',
      fill: 'tonexty',
      marker: {
        color: this.props.styling.charts.traceColors[0]
      }
    }];
  }

  getLayout(){
    return {
      xaxis: {
        title: this.__("Years"),
        type: 'category'
      },
      yaxis: {
        title: "%"
      }
    }
  }
}

PercentEbid.endpoint = 'percentTendersUsingEBid';
PercentEbid.getName = __ => __('Percent of tenders using eBid');
PercentEbid.getMaxField = pluckImm('percentageTendersUsingEbid');

export default PercentEbid;